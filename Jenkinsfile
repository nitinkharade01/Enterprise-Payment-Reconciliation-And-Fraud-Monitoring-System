pipeline {
  agent any

  environment {
    DOCKER_COMPOSE_FILE = 'docker/docker-compose.yml'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Root Spring Tests') {
      steps {
        sh 'mvn -B test'
      }
    }

    stage('Backend Microservice Tests') {
      steps {
        dir('backend') {
          sh 'mvn -B test'
        }
      }
    }

    stage('Frontend Tests') {
      steps {
        dir('frontend') {
          sh 'npm ci'
          sh 'npm test'
          sh 'npm run build'
        }
      }
    }

    stage('SonarQube Analysis') {
      steps {
        echo 'SonarQube analysis placeholder. Configure scanner credentials and quality gate policy here.'
      }
    }

    stage('Build Docker Images') {
      steps {
        sh "docker compose -f ${DOCKER_COMPOSE_FILE} build"
      }
    }

    stage('Push Docker Images') {
      when {
        expression { return env.DOCKER_REGISTRY != null && env.DOCKER_REGISTRY.trim() }
      }
      steps {
        echo 'Configure registry tagging and push commands for your target environment.'
      }
    }

    stage('Deploy') {
      when {
        branch 'main'
      }
      steps {
        echo 'Deploy stage placeholder for Render, Railway, ECS, EKS, or EC2 automation.'
      }
    }
  }

  post {
    always {
      junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml,frontend/junit.xml'
      archiveArtifacts allowEmptyArchive: true, artifacts: 'frontend/dist/**'
    }
  }
}
